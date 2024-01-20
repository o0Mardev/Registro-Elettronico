package com.mardev.registroelettronico.feature_main.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mardev.registroelettronico.feature_main.domain.model.Communication
import java.time.LocalDate

@Entity
data class CommunicationEntity(
    val title: String,
    val description: String,
    val date: LocalDate,
    val read: Boolean,

    @PrimaryKey val id: Int
) {
    fun toCommunication(): Communication{
        return Communication(
            id = id,
            title = title,
            description = description,
            date = date,
            read = read,
            attachments = null
        )
    }
}
