package com.example.demo.core

interface Aggregate : Entity {
    override val id: String
    override val version: Long?
}
