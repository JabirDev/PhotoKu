package com.jabirdev.core.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class UnsplashItem(
    @SerializedName("id")
    val id: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("width")
    val width: Int,

    @SerializedName("height")
    val height: Int,

    @SerializedName("color")
    val color: String,

    @SerializedName("blur_hash")
    val blurHash: String,

    @SerializedName("description")
    val description: String?,

    @SerializedName("alt_description")
    val altDescription: String,

    @SerializedName("urls")
    val urls: ImageUrl,

    @SerializedName("links")
    val links: UnsplashLink,

    @SerializedName("likes")
    val likes: Int,

    @SerializedName("liked_by_user")
    val likedByUser: Boolean,

    @SerializedName("user")
    val user: UnsplashUser,

    @SerializedName("tags")
    var tags: List<UnsplashTags>?,

    @SerializedName("views")
    var views: Int?,

    @SerializedName("downloads")
    var downloads: Int?,


) {

    data class ImageUrl(
        @SerializedName("raw")
        var raw: String,

        @SerializedName("full")
        var full: String,

        @SerializedName("regular")
        var regular: String,

        @SerializedName("small")
        var small: String,

        @SerializedName("thumb")
        var thumb: String,

        @SerializedName("small_s3")
        var smallS3: String
    )

    data class UnsplashLink(
        @SerializedName("self")
        var self: String,

        @SerializedName("html")
        var html: String,

        @SerializedName("download")
        var download: String,

        @SerializedName("download_location")
        var downloadLocation: String
    )

    data class UnsplashUser(
        @SerializedName("id")
        var id: String,

        @SerializedName("username")
        var username: String,

        @SerializedName("name")
        var name: String,

        @SerializedName("first_name")
        var firstName: String,

        @SerializedName("last_name")
        var lastName: String,

        @SerializedName("portfolio_url")
        var portfolioUrl: String,

        @SerializedName("bio")
        var bio: String,

        @SerializedName("links")
        var links: UserLink,

        @SerializedName("profile_image")
        var profileImage: ProfileImage,

        @SerializedName("social")
        var social: UnsplashSocial?
    )

    data class UnsplashTags (
        @SerializedName("title")
        var title: String,
    )

    data class UnsplashSocial(
        @SerializedName("instagram_username")
        var instagramUsername: String?,

        @SerializedName("portfolio_url")
        var portfolioUrl: String?,

        @SerializedName("twitter_username")
        var twitterUsername: String?,
    )

    data class ProfileImage(
        @SerializedName("small")
        var small: String,

        @SerializedName("medium")
        var medium: String,

        @SerializedName("large")
        var large: String
    )

    data class UserLink(
        @SerializedName("self")
        var self: String,

        @SerializedName("html")
        var html: String,

        @SerializedName("photos")
        var photos: String,

        @SerializedName("likes")
        var likes: String,

        @SerializedName("portfolio")
        var portfolio: String,

        @SerializedName("following")
        var following: String,

        @SerializedName("followers")
        var followers: String
    )

}
