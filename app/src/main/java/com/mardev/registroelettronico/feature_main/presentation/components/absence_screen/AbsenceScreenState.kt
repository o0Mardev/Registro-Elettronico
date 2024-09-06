package com.mardev.registroelettronico.feature_main.presentation.components.absence_screen

import com.mardev.registroelettronico.feature_main.domain.model.GenericAbsence

data class AbsenceScreenState(

    val genericAbsences: List<GenericAbsence> = emptyList(),

    val absences: List<GenericAbsence> = emptyList(),
    val delays: List<GenericAbsence> = emptyList(),
    val exits: List<GenericAbsence> = emptyList(),

    val loading: Boolean = true
)
