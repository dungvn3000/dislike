package models

/**
 * The Class Permission.
 *
 * @author Nguyen Duc Dung
 * @since 2/26/13 2:28 PM
 *
 */
sealed trait Permission

case object Administrator extends Permission

case object NormalUser extends Permission