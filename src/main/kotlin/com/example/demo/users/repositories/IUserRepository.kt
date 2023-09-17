package com.example.demo.users.repositories

import com.example.demo.users.models.User
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.ListPagingAndSortingRepository
import org.springframework.data.repository.Repository

interface IUserRepository : ListPagingAndSortingRepository<User, String>, CrudRepository<User, String>,
    Repository<User, String> {
        fun findByName(name: String): User?
    }
