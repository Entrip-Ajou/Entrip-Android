package ajou.paran.entrip.screen.recommendation

data class UsersTravelFavoriteSaveRequestDto(
    var regions : MutableList<String>,
    var scores : MutableList<Int>
)