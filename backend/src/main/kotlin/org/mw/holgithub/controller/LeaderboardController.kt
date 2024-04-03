package org.mw.holgithub.controller

import org.mw.holgithub.service.LeaderboardEntry
import org.mw.holgithub.service.LeaderboardService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/leaderboard")
class LeaderboardController(private val service: LeaderboardService) {
    @GetMapping
    fun getLeaderboard(): List<LeaderboardEntry> {
        return service.getUserScores()
    }
}