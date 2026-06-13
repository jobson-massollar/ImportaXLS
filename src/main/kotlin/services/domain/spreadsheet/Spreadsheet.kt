package services.domain.spreadsheet

interface Spreadsheet {
    fun readAsSequence(): Sequence<List<String>>
}