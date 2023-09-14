package com.shisanfan.shisanfanauthspring.user.domain

import com.shisanfan.shisanfanauthspring.common.AggregateRoot
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class User(
    @Id override val id: String,
    val name: String,
    val password: String,
) : AggregateRoot {}
