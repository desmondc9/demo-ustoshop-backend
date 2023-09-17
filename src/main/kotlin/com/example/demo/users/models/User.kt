package com.example.demo.users.models

import com.example.demo.core.Aggregate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class User(
    @Id override val id: String,
    @Version override val version: Long? = null,
    @Indexed(unique = true) val name: String,
    val password: String,
) : Aggregate
