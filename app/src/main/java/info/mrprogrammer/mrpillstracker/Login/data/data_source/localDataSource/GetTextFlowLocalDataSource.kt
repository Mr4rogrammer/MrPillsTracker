package info.mrprogrammer.mrpillstracker.Login.data.data_source.localDataSource

import info.mrprogrammer.mrpillstracker.Login.data.data_source.GetTextFlowDataSource

class GetTextFlowLocalDataSource: GetTextFlowDataSource {
    override suspend fun getTextFlowData(): List<String> {
        return listOf(
            "Children's ❤\uFE0F",
            "Parent's \uD83D\uDE0D",
            "all your loved ones \uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC66"
        )
    }
}