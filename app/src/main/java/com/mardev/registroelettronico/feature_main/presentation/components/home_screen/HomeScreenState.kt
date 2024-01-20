package com.mardev.registroelettronico.feature_main.presentation.components.home_screen

import com.mardev.registroelettronico.feature_main.domain.model.DailyEvents
import java.time.LocalDate

data class HomeScreenState(
    val date: LocalDate = LocalDate.now(),
    val events: DailyEvents = DailyEvents(emptyList(), emptyList(), emptyList(), emptyList()),
    val loading: Boolean = true
)
