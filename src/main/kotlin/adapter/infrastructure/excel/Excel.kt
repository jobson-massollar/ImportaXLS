package adapter.infrastructure.excel

import org.apache.poi.ss.usermodel.DataFormatter
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import services.domain.spreadsheet.Spreadsheet
import java.io.FileInputStream

class Excel(val path: String,
            val dropLines: Int = 1,
            val sheetName: String = "Sheet"): Spreadsheet {

    private val _null = "NULL"

    override fun readAsSequence(): Sequence<List<String>> = sequence {

        FileInputStream(path).use { fis ->
            XSSFWorkbook(fis).use { workbook ->

                val sheet = workbook.getSheet(sheetName)
                val formatter = DataFormatter()

                sheet.drop(dropLines).forEach { row ->

                    val line = (0 until row.lastCellNum)
                        .map { i ->

                            //if (layout[i].type != XLSDataType.EXCLUDE) {
                            val cell = row.getCell(i, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL)

//                            if (cell != null) {
//                                println("${i} => ${cell.cellType}")
//                            }

                            if (cell != null) formatter.formatCellValue(cell) else _null
                            //}
                        }

                    yield(line)
                }
            }
        }
    }
}