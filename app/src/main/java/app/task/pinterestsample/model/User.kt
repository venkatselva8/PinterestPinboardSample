package app.task.pinterestsample.model

import com.google.gson.annotations.SerializedName

data class User(

	@field:SerializedName("profile_image")
	val profileImage: ProfileImage? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("links")
	val links: Links? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)