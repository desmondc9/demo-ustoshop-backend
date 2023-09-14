package com.example.demo.user.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class User(
    @Id val id: String,
    val name: String,
    val password: String,
)
