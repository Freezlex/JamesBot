package com.freezlex.kohanato.core.extensions

import com.freezlex.kohanato.core.KohanatoCore
import com.freezlex.kohanato.core.i18n.Language
import com.freezlex.kohanato.core.i18n.LanguageModel
import com.freezlex.kohanato.core.i18n.Test
import net.dv8tion.jda.api.events.Event
import net.dv8tion.jda.api.events.interaction.command.GenericCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.CommandInteraction
import java.text.Normalizer
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

fun String.asDuration(): Duration? {
    val duration = this.filter { it.isDigit() }.toInt()
    val unit = this.removePrefix(this.filter { it.isDigit() })
    return when(unit.uppercase()){
        "D" -> duration.days
        "M" -> duration.minutes
        "S" -> duration.seconds
        "MS" -> duration.milliseconds
        else -> null
    }
}

/*fun CommandInteraction.localize{
    Language.localize();
}*/

var KohanatoCore.test: String
    get() = "test"
    set(value) = TODO()

/**
 * Escape potential regex from a string
 * @param string
 *          The string to escape
 */
fun escapeRegex(string: String): String{
    return string.replace(Regex("""[|\\{}()[\\]^$+*?.]""")) {m -> "\\${m.value}"}
}

/**
 * Simple levenshtein method to compare two strings
 * @param tc List of CharSequence ton compare to cp
 * @param cp CharSequence as comparison point
 */
fun findBestMatch(tc: List<CharSequence>, cp: CharSequence) : List<String> {

    val temp = HashMap<String, Int>() // HashMap<Command Name, Matching factor>

    for(c in tc){
        val cLength = c.length + 1
        val cpLength = cp.length + 1

        var cost = Array(cLength) { it }
        var newCost = Array(cLength) { 0 }

        for (i in 1 until cpLength) {
            newCost[0] = i

            for (j in 1 until cLength) {
                val match = if(c[j - 1] == cp[i - 1]) 0 else 1

                val costReplace = cost[j - 1] + match
                val costInsert = cost[j] + 1
                val costDelete = newCost[j - 1] + 1

                newCost[j] = costInsert.coerceAtMost(costDelete).coerceAtMost(costReplace)
            }

            val swap = cost
            cost = newCost
            newCost = swap
        }

        if(cost[cLength - 1] != c.length) temp[c.toString()] = (c.length - cost[cLength - 1])
    }

    return temp.filter { it.value == temp.values.maxOrNull() }.map { it.key }
}

fun convertText(s: String): String {
    var finalString = ""
    val accents = "éàèùâêîôû0123456789 "
    var t = s
    t = Normalizer.normalize(t, Normalizer.Form.NFC)
    t = Normalizer.normalize(t, Normalizer.Form.NFD)
    t = Normalizer.normalize(t, Normalizer.Form.NFKC)
    t = Normalizer.normalize(t, Normalizer.Form.NFKD)
    t.forEach {
        finalString += if (accents.contains(it)) it else convertChar(it)
    }
    return finalString.replace("\u200B", "")
}
fun convertChar(c: Char): Char {
    val list: List<String> = listOf(
        "\uDD1E\uDD1F\uDD20\uDD21\uDD22\uDD23\uDD24\uDD25\uDD26\uDD27\uDD28\uDD29\uDD2A\uDD2B\uDD2C\uDD2D\uDD2E\uDD2F\uDD30\uDD31\uDD32\uDD33\uDD34\uDD35\uDD36\uDD37",
        "\uDD86\uDD87\uDD88\uDD89\uDD8A\uDD8B\uDD8C\uDD8D\uDD8E\uDD8F\uDD90\uDD91\uDD92\uDD93\uDD94\uDD95\uDD96\uDD97\uDD98\uDD99\uDD9A\uDD9B\uDD9C\uDD9D\uDD9E\uDD9F",
        "\uDCEA\uDCEB\uDCEC\uDCED\uDCEE\uDCEF\uDCF0\uDCF1\uDCF2\uDCF3\uDCF4\uDCF5\uDCF6\uDCF7\uDCF8\uDCF9\uDCFA\uDCFB\uDCFC\uDCFD\uDCFE\uDCFF\uDD00\uDD01\uDD02\uDD03",
        "\uDCB6\uDCB7\uDCB8\uDCB9\uDC52\uDCBB\uDC54\uDCBD\uDCBE\uDCBF\uDCC0\uDCC1\uDCC2\uDCC3\uDC5C\uDCC5\uDCC6\uDCC7\uDCC8\uDCC9\uDCCA\uDCCB\uDCCC\uDCCD\uDCCE\uDCCF",
        "\uDD52\uDD53\uDD54\uDD55\uDD56\uDD57\uDD58\uDD59\uDD5A\uDD5B\uDD5C\uDD5D\uDD5E\uDD5F\uDD60\uDD61\uDD62\uDD63\uDD64\uDD65\uDD66\uDD67\uDD68\uDD69\uDD6A\uDD6B",
        "\uDD30\uDD31\uDD32\uDD33\uDD34\uDD35\uDD36\uDD37\uDD38\uDD39\uDD3A\uDD3B\uDD3C\uDD3D\uDD3E\uDD3F\uDD40\uDD41\uDD42\uDD43\uDD44\uDD45\uDD46\uDD47\uDD48\uDD49",
        "\uDD70\uDD71\uDD72\uDD73\uDD74\uDD75\uDD76\uDD77\uDD78\uDD79\uDD7A\uDD7B\uDD7C\uDD7D\uDD7E\uDD7F\uDD80\uDD81\uDD82\uDD83\uDD84\uDD85\uDD86\uDD87\uDD88\uDD89",
        "ａｂｃｄｅｆｇｈｉｊｋｌｍｎｏｐｑｒｓｔｕｖｗｘｙｚ",
        "ᴀʙᴄᴅᴇꜰɢʜɪᴊᴋʟᴍɴᴏᴘQʀꜱᴛᴜᴠᴡxʏᴢ",
        "zʎxʍʌnʇsɹbdouɯlʞɾıɥɓɟǝpɔqɐ",
        "ɐqɔpǝɟɓɥıɾʞlɯuodbɹsʇnʌʍxʎz",
        "ₐbcdₑfgₕᵢⱼₖₗₘₙₒₚqᵣₛₜᵤᵥwₓyz",
        "ᵃᵇᶜᵈᵉᶠᵍʰⁱʲᵏˡᵐⁿᵒᵖqʳˢᵗᵘᵛʷˣʸᶻ",
        "ⓐⓑⓒⓓⓔⓕⓖⓗⓘⓙⓚⓛⓜⓝⓞⓟⓠⓡⓢⓣⓤⓥⓦⓧⓨⓩ",
        "ค๒ς๔єŦﻮђเןкɭ๓ภ๏קợгรՇยשฬאץչ",
        "αႦƈԃҽϝɠԋιʝƙʅɱɳσρϙɾʂƚυʋɯxყȥ",
        "ǟɮƈɖɛʄɢɦɨʝӄʟʍռօքզʀֆȶʊʋաӼʏʐ",
        "ᏗᏰፈᎴᏋᎦᎶᏂᎥᏠᏦᏝᎷᏁᎧᎮᎤᏒᏕᏖᏬᏉᏇጀᎩፚ",
        "ąცƈɖɛʄɠɧıʝƙƖɱŋơ℘զཞʂɬų۷ῳҳყʑ",
        "ค๖¢໓ēfງhiวkl๓ຖ໐p๑rŞtนงຟxฯຊ",
        "a҉b҉c҉d҉e҉f҉g҉h҉i҉j҉k҉l҉m҉n҉o҉p҉q҉r҉s҉t҉u҉v҉w҉x҉y҉z҉",
        "丹书匚刀巳下呂廾工丿片乚爪冂口尸Q尺丂丁凵V山乂Y乙",
        "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    )
    val alphabet = "abcdefghijklmnopqrstuvwxyz"
    val majAlphabet = alphabet.uppercase()
    var returned = '\u200B'
    list.forEach {
        if (it.contains(c))returned = if (c.isUpperCase()) majAlphabet[it.indexOf(c)%26] else alphabet[it.indexOf(c)%26]
    }
    return returned
}