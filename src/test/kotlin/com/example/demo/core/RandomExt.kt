package com.example.demo.core

import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils
import kotlin.random.Random

fun Random.nextString(count: Int = 10) = RandomStringUtils.randomAlphabetic(count)
