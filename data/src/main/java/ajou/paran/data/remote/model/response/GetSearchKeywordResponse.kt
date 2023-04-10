package ajou.paran.data.remote.model.response

typealias GetSearchKeywordResponseList = List<GetSearchKeywordResponse>

data class GetSearchKeywordResponse(
    // 장소명, 업체명
    val place_name: String,
    // 전체 도로명 주소
    val road_address_name: String,
    // X 좌표값 혹은 longitude
    val x: String,
    // Y 좌표값 혹은 latitude
    val y: String,
)
