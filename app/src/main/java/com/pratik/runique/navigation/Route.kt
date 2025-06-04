package com.pratik.runique.navigation

object Route {
    object Authentication {
        const val AUTH = "auth"
        const val INTRO = "intro"
        const val REGISTER = "register"
        const val LOGIN = "login"
    }

    object RunTracker {
        const val RUN_OVERVIEW = "run_overview"
        const val RUN = "run"
        const val ACTIVE_RUN = "active_run"
    }

    object Deeplinks {
        const val ACTIVE_RUN_SCREEN = "runique://active_run"
    }
}