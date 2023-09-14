package com.example.demo.user.infrastructure

import com.example.demo.user.domain.User
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.data.repository.Repository

interface IUserRepository : ListPagingAndSortingRepository<User, String>, CrudRepository<User, String>,
    Repository<User, String>

