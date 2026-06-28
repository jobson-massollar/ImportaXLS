package model

import services.domain.persistence.IDAO
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance
import kotlin.uuid.Uuid

interface IRepository<T: Entity> {
    fun save(e: T)
    fun remove(e: T)
    fun findAll(): List<T>
    fun deleteAll()
    fun startBatchInsert(chunkSize: Int)
    fun batchInsert(e: T)
    fun endBatchInsert()
}

abstract class Repository<T: Entity, S: IDAO<V>, V: EntityDTO>: IRepository<T> {

    private var chunkSize: Int = 0
    private var chunk: MutableList<V>? = null

    abstract val dao: S
    abstract fun createDTO(e: T): V
    abstract fun createEntity(dto: V): T

    override fun save(e: T) {
        if (e.id == null) {
            e.id = Uuid.random()
            dao.insert(createDTO(e))
        }
        else
            dao.update(createDTO(e))
    }

    override fun remove(e: T) {
        if (e.id != null) {
            dao.delete(createDTO(e))
            e.id = null
        }
    }

    override fun findAll(): List<T> =
        dao.findAll().map {
            val c = createEntity(it)
            c.id = it.id
            c
        }.toList()

    override fun deleteAll() {
        dao.deleteAll()
    }

    override fun startBatchInsert(chunkSize: Int) {
        if (chunkSize > 0) {
            this.chunkSize = chunkSize
            chunk = mutableListOf()
        }
    }

    override fun batchInsert(e: T) {
        chunk?.let {
            if (e.id == null)
                e.id = Uuid.random()
            it.add(createDTO(e))
            if (it.size == chunkSize) {
                dao.batchInsert(it)
                it.clear()
            }
        }
    }

    override fun endBatchInsert() {
        chunk?.let {
            if (it.size > 0) {
                dao.batchInsert(it)
                it.clear()
            }
        }
        chunk = null
    }
}

interface IRepositoryFactory {
    fun <T:Repository<*,*,*>> get(c: KClass<in T>): T
}

object RepositoryFactory : IRepositoryFactory {

    private val repositories = HashMap<String, Repository<*, *, *>>()

    override fun <T:Repository<*,*,*>> get(c: KClass<in T>): T {
        if (! repositories.containsKey(c.java.name))
            repositories[c.java.name] = c.createInstance() as T
        return repositories[c.java.name] as T
    }
}