package org.jogilvie.mailparser

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.apache.james.mime4j.dom.Message
import org.apache.james.mime4j.dom.address.Address
import org.apache.james.mime4j.field.address.AddressFormatter
import org.apache.james.mime4j.message.MessageImpl
import org.apache.james.mime4j.util.MimeUtil
import java.io.ByteArrayInputStream
import java.text.SimpleDateFormat


@Serializable
class EmailHeaders(
    val To: String,
    val From: String,
    val Date: String,
    val Subject: String,
    @SerialName("Message-Id")
    val MessageId: String){

    constructor(message: Message): this(
        To = formatAddress(message.to.first()),
        From = formatAddress(message.from.first()),

        // the works-out-of-the-box answer here is to be a bit lenient in the time zone of the
        // resulting date, but that's not 100% up to spec for the task:
        // Date = MimeUtil.formatDate(message.date, null),
        // yields (accurate) dates but in my time zone since there's no real TZ info in java date,
        // we will do something dirtier but more correct:
        Date = formatDate(message),

        Subject = message.subject,

        // could SerialName all of these to conform to kt convention of camel case attr names
        // (i.e. 'subject' serializes to 'Subject'), but omitted for brevity
        MessageId = message.messageId
    )

    companion object {
        val dateFormat = SimpleDateFormat()

        fun fromString(payload: String): EmailHeaders {
            val stream = ByteArrayInputStream(payload.toByteArray(charset("UTF-8")))
            val message = Message.Builder.of(stream).build()
            return EmailHeaders(message)
        }

        fun formatAddress(address: Address): String {
            // this is only complicated because for some reason the overload for formatting Address (versus subclasses
            // Mailbox or Group) requires a StringBuilder.  Makes it kinda painful to do inline.
            val sb = StringBuilder()
            AddressFormatter.DEFAULT.format(sb, address, false)
            return sb.toString()
        }

        fun formatDate(message: Message): String {
            // I don't super like that we're digging into guts instead of interfaces, but here goes.
            with(message as MessageImpl){
                return message.header.getField("Date").body
            }
        }
    }
}