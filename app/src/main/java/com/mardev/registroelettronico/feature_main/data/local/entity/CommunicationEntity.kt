package com.mardev.registroelettronico.feature_main.common.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mardev.registroelettronico.feature_main.common.domain.model.Communication
import java.util.Date

@Entity
data class CommunicationEntity(
    val title: String,
    val description: String,
    val date: Date,
    val read: Boolean,

    @PrimaryKey val id: Int
) {
    fun toCommunication(): Communication {
        return Communication(
            title = title,
            description = description,
            date = date,
            read = read,
            attachments = null
        )
    }
}
