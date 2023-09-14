package com.shisanfan.shisanfanauthspring.user.infrastructure

import com.shisanfan.shisanfanauthspring.user.domain.User
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository

interface IUserRepository : ListPagingAndSortingRepository<User, String>, CrudRepository<User, String>

