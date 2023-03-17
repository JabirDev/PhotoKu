package com.jabirdev.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UnsplashDetail(
    val id: String,
    val createdAt: String,
    val updatedAt: String,
    val width: Int,
    val height: Int,
    val color: String,
    val blurHash: String,
    val description: String?,
    val altDescription: String?,
    val urls: ImageUrl,
    val links: UnsplashLink,
    val likes: Int,
    val likedByUser: Boolean,
    val user: UnsplashUser,
    var tags: List<UnsplashTags>?,
    var views: Int?,
    var downloads: Int?
) : Parcelable {

    @Parcelize
    data class ImageUrl(
        var raw: String,
        var full: String,
        var regular: String,
        var small: String,
        var thumb: String,
        var smallS3: String
    ): Parcelable

    @Parcelize
    data class UnsplashLink(
        var self: String,
        var html: String,
        var download: String,
        var downloadLocation: String
    ): Parcelable

    @Parcelize
    data class UnsplashUser(
        var id: String,
        var username: String,
        var name: String,
        var firstName: String,
        var lastName: String?,
        var portfolioUrl: String?,
        var bio: String?,
        var links: UserLink,
        var profileImage: ProfileImage,
        var social: UnsplashSocial?
    ): Parcelable

    @Parcelize
    data class UnsplashTags (
        var title: String,
    ): Parcelable

    @Parcelize
    data class UnsplashSocial(
        var instagramUsername: String?,
        var portfolioUrl: String?,
        var twitterUsername: String?,
    ): Parcelable

    @Parcelize
    data class ProfileImage(
        var small: String,
        var medium: String,
        var large: String
    ): Parcelable

    @Parcelize
    data class UserLink(
        var self: String,
        var html: String,
        var photos: String,
        var likes: String,
        var portfolio: String,
        var following: String,
        var followers: String
    ): Parcelable

}