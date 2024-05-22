package info.mrprogrammer.mrpillstracker.Login.data.repository

import info.mrprogrammer.mrpillstracker.Login.data.data_source.GetTextFlowDataSource
import info.mrprogrammer.mrpillstracker.Login.domain.repository.GetTextFlowDataRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTextFlowDataRepositoryImpl @Inject constructor(private val getTextFlowDataSource: GetTextFlowDataSource) :
    GetTextFlowDataRepository {

    override suspend fun getTextFlowData(): Flow<String> {
        val delay = 200L
        val data = getTextFlowDataSource.getTextFlowData()
        val first = data.first()
        val second = data[1]
        val third = data[2]
        return flow {
            while (true) {
                var result = ""

                first.forEach {
                    result += it
                    emit("$result |")
                    delay(delay)
                }

                delay(200L)

                first.forEachIndexed { index, c ->
                    result = result.drop(index)
                    emit("$result |")
                    delay(delay)
                }

                result = ""

                second.forEach {
                    result += it
                    emit("$result |")
                    delay(delay)
                }

                delay(200L)

                second.forEachIndexed { index, c ->
                    result = result.drop(index)
                    emit("$result |")
                    delay(delay)
                }

                result = ""

                third.forEach {
                    result += it
                    emit("$result |")
                    delay(delay)
                }

                delay(200L)

                third.forEachIndexed { index, c ->
                    result = result.drop(index)
                    emit("$result |")
                    delay(delay)
                }
            }
        }
    }
}