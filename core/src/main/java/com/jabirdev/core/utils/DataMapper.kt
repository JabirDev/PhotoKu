package com.jabirdev.core.utils

import com.jabirdev.core.data.source.local.entity.UnsplashEntity
import com.jabirdev.core.data.source.local.entity.UnsplashFavorite
import com.jabirdev.core.data.source.remote.response.UnsplashItem
import com.jabirdev.core.domain.model.Unsplash
import com.jabirdev.core.domain.model.UnsplashDetail
import retrofit2.Response

object DataMapper {

    fun UnsplashEntity.toDomain(): Unsplash =
        Unsplash(
            id = this.id,
            isFavorite = this.isFavorite,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            rawImage = this.rawImage,
            fullImage = this.fullImage,
            regularImage = this.regularImage,
            smallImage = this.smallImage,
            thumbImage = this.thumbImage,
            smallS3Image = this.smallS3Image,
            selfLink = this.selfLink,
            htmlLink = this.htmlLink
        )

    fun UnsplashFavorite.toDomain(): Unsplash =
        Unsplash(
            id = this.id,
            isFavorite = true,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            rawImage = this.rawImage,
            fullImage = this.fullImage,
            regularImage = this.regularImage,
            smallImage = this.smallImage,
            thumbImage = this.thumbImage,
            smallS3Image = this.smallS3Image,
            selfLink = this.selfLink,
            htmlLink = this.htmlLink
        )

    fun UnsplashItem.toDomainDetail(): UnsplashDetail {
        val imgUrl = UnsplashDetail.ImageUrl(
            raw = this.urls.raw,
            full = this.urls.full,
            regular = this.urls.regular,
            small = this.urls.small,
            thumb = this.urls.thumb,
            smallS3 = this.urls.smallS3
        )
        val links = UnsplashDetail.UnsplashLink(
            self = this.links.self,
            html = this.links.html,
            download = this.links.download,
            downloadLocation = this.links.downloadLocation
        )
        val userLink = UnsplashDetail.UserLink(
            self = this.user.links.self,
            html = this.user.links.html,
            photos = this.user.links.photos,
            likes = this.user.links.likes,
            portfolio = this.user.links.portfolio,
            following = this.user.links.following,
            followers = this.user.links.followers
        )
        val imgProfile = UnsplashDetail.ProfileImage(
            small = this.user.profileImage.small,
            medium = this.user.profileImage.medium,
            large = this.user.profileImage.large
        )
        val social = UnsplashDetail.UnsplashSocial(
            instagramUsername = this.user.social?.instagramUsername,
            portfolioUrl = this.user.social?.portfolioUrl,
            twitterUsername = this.user.social?.twitterUsername
        )
        val user = UnsplashDetail.UnsplashUser(
            id = this.user.id,
            username = this.user.username,
            name = this.user.name,
            firstName = this.user.firstName,
            lastName = this.user.lastName,
            portfolioUrl = this.user.portfolioUrl,
            bio = this.user.bio,
            links = userLink,
            profileImage = imgProfile,
            social = social
        )
        val tags = ArrayList<UnsplashDetail.UnsplashTags>()
        this.tags?.forEach {
            val tag = UnsplashDetail.UnsplashTags(it.title)
            tags.add(tag)
        }
        return UnsplashDetail(
            id = this.id,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            width = this.width,
            height = this.height,
            color = this.color,
            blurHash = this.blurHash,
            description = this.description,
            altDescription = this.altDescription,
            urls = imgUrl,
            links = links,
            likes = this.likes,
            likedByUser = this.likedByUser,
            user = user,
            tags = tags,
            views = this.views,
            downloads = this.downloads
        )
    }

    fun UnsplashItem.toEntity(isFavorite: Boolean): UnsplashEntity {
        return UnsplashEntity(
            id = this.id,
            isFavorite = isFavorite,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            rawImage = this.urls.raw,
            fullImage = this.urls.full,
            regularImage = this.urls.regular,
            smallImage = this.urls.small,
            thumbImage = this.urls.thumb,
            smallS3Image = this.urls.smallS3,
            selfLink = this.links.self,
            htmlLink = this.links.html
        )
    }

    fun Unsplash.toEntity(): UnsplashEntity =
        UnsplashEntity(
            id = this.id,
            isFavorite = this.isFavorite,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            rawImage = this.rawImage,
            fullImage = this.fullImage,
            regularImage = this.regularImage,
            smallImage = this.smallImage,
            thumbImage = this.thumbImage,
            smallS3Image = this.smallS3Image,
            selfLink = this.selfLink,
            htmlLink = this.htmlLink
        )

    fun Unsplash.toFavoriteEntity(): UnsplashFavorite =
        UnsplashFavorite(
            id = this.id,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt,
            rawImage = this.rawImage,
            fullImage = this.fullImage,
            regularImage = this.regularImage,
            smallImage = this.smallImage,
            thumbImage = this.thumbImage,
            smallS3Image = this.smallS3Image,
            selfLink = this.selfLink,
            htmlLink = this.htmlLink
        )

    fun Response<UnsplashItem>.toResponseDetail(): Response<UnsplashDetail> {
        val item = this.body()?.toDomainDetail()
        return if (this.isSuccessful){
            Response.success(item)
        } else {
            Response.error(this.code(), this.errorBody()!!)
        }
    }

}