package com.example.scoot_scoot

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform