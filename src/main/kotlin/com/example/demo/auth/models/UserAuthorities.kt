package com.example.demo.auth.models

import com.example.demo.core.Entity
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class UserAuthorities(
    @Id override val id: String,
    @Version override val version: Long?,
    @Indexed(unique = true) val userId: String,
    val authorities: List<Authority>,
): Entity
