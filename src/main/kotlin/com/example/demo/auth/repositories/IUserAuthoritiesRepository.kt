package com.example.demo.auth.repositories

import com.example.demo.auth.models.UserAuthorities
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.Repository
import java.util.Optional

interface IUserAuthoritiesRepository : PagingAndSortingRepository<UserAuthorities, String>, CrudRepository<UserAuthorities, String>,
    Repository<UserAuthorities, String> {
        fun findByUserId(userId: String): Optional<UserAuthorities>

    }

