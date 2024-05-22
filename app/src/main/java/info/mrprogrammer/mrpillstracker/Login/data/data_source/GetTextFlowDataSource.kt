package info.mrprogrammer.mrpillstracker.Login.data.data_source

interface GetTextFlowDataSource {
    suspend fun getTextFlowData() : List<String>
}