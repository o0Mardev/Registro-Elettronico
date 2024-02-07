package com.mardev.registroelettronico.core.domain.use_case

import com.mardev.registroelettronico.R
import com.mardev.registroelettronico.core.util.Resource
import com.mardev.registroelettronico.core.util.UIText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import retrofit2.HttpException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class SaveUpdateUseCase {
    operator fun invoke(body: ResponseBody, file: File): Flow<Resource<Float>> {
        var progress: Float
        val inputStream = body.byteStream()
        val outputStream = FileOutputStream(file)

        val returnFlow = flow<Resource<Float>> {

            try {
                inputStream.use { input ->
                    outputStream.use { output ->
                        val totalBytes = body.contentLength()
                        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                        var progressBytes = 0f
                        var bytes = input.read(buffer)
                        while (bytes >= 0) {
                            output.write(buffer, 0, bytes)
                            progressBytes += bytes
                            bytes = input.read(buffer)
                            progress = (progressBytes / totalBytes)
                            emit(Resource.Loading(progress))
                        }
                        emit(Resource.Success(1f))
                    }
                }
            } catch (e: HttpException) {
                emit(
                    Resource.Error(UIText.StringResource(R.string.error1))
                )
            } catch (e: IOException) {
                emit(
                    Resource.Error(UIText.StringResource(R.string.error2))
                )
            }
        }
            .flowOn(Dispatchers.IO)
        return returnFlow
    }
}