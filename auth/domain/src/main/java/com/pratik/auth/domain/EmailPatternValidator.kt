package com.pratik.auth.domain

interface EmailPatternValidator {
    fun matches(email:String): Boolean
}