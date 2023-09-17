package com.example.demo.core

import org.springframework.data.annotation.Version

interface Aggregate: Entity {
    override val id: String
    override val version: Long?
}
