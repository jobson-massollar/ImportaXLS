package model

import kotlin.uuid.Uuid

abstract class Entity {
    var id: Uuid? = null
}

abstract class EntityDTO(val id: Uuid?)