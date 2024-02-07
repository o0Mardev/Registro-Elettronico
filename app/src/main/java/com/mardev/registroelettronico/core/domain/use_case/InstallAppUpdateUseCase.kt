package com.mardev.registroelettronico.core.domain.use_case

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
import androidx.core.content.FileProvider
import com.mardev.registroelettronico.BuildConfig
import java.io.File

class InstallAppUpdateUseCase {
    operator fun invoke(file: File, context: Context) {
        installUpdateFile(file, context)
    }

    private fun installUpdateFile(file: File, context: Context) {
        if (file.exists()) {
            val uri = FileProvider.getUriForFile(
                context,
                BuildConfig.APPLICATION_ID + ".provider",
                file
            )
            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(uri, "application/vnd.android.package-archive")
            intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_GRANT_READ_URI_PERMISSION
            context.startActivity(intent)
        }
    }

}