package hr.gregl.goldenhourphotography.model
data class Item(
    var _id: Long?,
    val title: String,
    val explanation: String,
    val picturePath: String,
    val date: String,
    val read: Boolean
)
