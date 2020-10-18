package com.remember.app.data.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
public data class Moe(
        @SerializedName("biography")
        val biography: String = "", // Эдуард Хиль - один из известнейших голосов советской сцены. Вообще, он имел немалую славу и заграницей, где его прекрасный голос и красивая манера пения пришлись многим по вкусу. &nbsp;&nbsp;В 1949 году Хиль решает начать учебу. Выбирает он для этого чудесный город Ленинград, где проживал его дядя. Для начала он решил получить рабочее образование, чтобы как можно раньше съехать от родственника. Хиль начинает работать, параллельно посещать музыкальную школу, а затем и консерваторию.&nbsp;Тут и начинается самое интересное. 1963 становится отправной точкой в творческом пути Эдуарда Хиля. Его голос смог сразу очаровать людей, его слава стала расти. Далее он начинает посещать творческие конкурсы, где занимает призовые места. В 1965 году на фестивале советской песни Хиль очень хорошо себя показал, поэтому отправился покорять Европу. В этом же году он порадовал поклонников вторым местом в Польше и четвёртым местом в Бразилии, что огромный успех для относительно начинающего певца. Затем он получает звание народного артиста РСФСР, утверждая этим свой статус. Известнейшие его композиции - “Trololo” и «У леса на опушке» звучали отовсюду!&nbsp;В плане личной жизни, у артиста все сложилось отлично. У него была одна жена и любимая дочь.&nbsp;Причиной смерти стал инсульт. Похоронен Эдуард Хиль на Смоленском кладбище, захоронение прошло спустя три дня после трагедии. На могиле Эдуарда Хиля установлен памятник к его восьмидесятилетию, который ежедневно напоминает, что его творчество ещё живет!
        @SerializedName("comment")
        val comment: String = "", // Самый лучший папа..навсегда в сердце..
        @SerializedName("coords")
        val coords: String? = "",
        @SerializedName("created_at")
        val createdAt: String = "", // 2020-10-16T18:03:01.000Z
        @SerializedName("creator_data")
        val creatorData: CreatorData?,
        @SerializedName("datarod")
        val datarod: String = "", // 1947-10-07
        @SerializedName("datasmert")
        val datasmert: String = "", // 2020-09-07
        @SerializedName("deleted_at")
        val deletedAt: String? = "", // null
        @SerializedName("flag")
        val flag: String = "", // true
        @SerializedName("gorod")
        val gorod: String = "", // с.Майкопское
        @SerializedName("id")
        val id: Int?, // 1469
        @SerializedName("name")
        val name: String = "", // Иван
        @SerializedName("nazvaklad")
        val nazvaklad: String = "", // Майкопское
        @SerializedName("nummogil")
        val nummogil: String = "",
        @SerializedName("oblast")
        val oblast: String = "",
        @SerializedName("picture")
        val picture: String = "", // /uploads/pages/1468/1602701220501.form-data
        @SerializedName("picture_data")
        val pictureData: String = "", // /uploads/pages/1468/1602701220501.form-data
        @SerializedName("rajon")
        val rajon: String = "",
        @SerializedName("religiya")
        val religiya: String? = "", // Православие
        @SerializedName("secondname")
        val secondname: String = "", // Лусто
        @SerializedName("sector")
        val sector: String = "null",
        @SerializedName("star")
        val star: String = "", // false
        @SerializedName("status")
        val status: String = "", // Одобрено
        @SerializedName("thirtname")
        val thirtname: String = "", // Дмитриевич
        @SerializedName("uchastok")
        val uchastok: String? = "",
        @SerializedName("updated_at")
        val updatedAt: String? = "", // 2020-10-16T18:03:01.000Z
        @SerializedName("user_id")
        val userId: String = "",// 2479
        var isLoaded: Boolean = false,
        var isShowMore: Boolean = false
) : Parcelable {

    @Parcelize
    data class CreatorData(
            @SerializedName("name")
            val name: String? = "", // 358803230
            @SerializedName("picture")
            val picture: String? = "", // https://sun1-95.userapi.com/impg/c854128/v854128168/1d9f27/OF6QdIa71h8.jpg?size=400x0&quality=90&crop=2,0,1197,1197&sign=c9a4b720ca1e56908d2c3d1077fb4aea&c_uniq_tag=GJduvMqLAdzZUZ_Nvxg3cQJxD9nxZXxr0U76wZd-yoA&ava=1
            @SerializedName("settings_name")
            val settingsName: String? = "" // Инна
    ) : Parcelable
}