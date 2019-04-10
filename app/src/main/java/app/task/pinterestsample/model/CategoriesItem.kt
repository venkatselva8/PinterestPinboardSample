package app.task.pinterestsample.model

import com.google.gson.annotations.SerializedName

data class CategoriesItem(

	@field:SerializedName("photo_count")
	val photoCount: Int? = null,

	@field:SerializedName("links")
	val links: Links? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("title")
	val title: String? = null
)