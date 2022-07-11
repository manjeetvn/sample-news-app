package com.example.app.common

class Constant {

    companion object {
        const val USER_REGEX = "^[0-9a-zA-Z]*$"
        const val PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@!?_])(?=\\S+\$).{5,}\$"
    }
}