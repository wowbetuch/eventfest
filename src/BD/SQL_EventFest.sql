
/**
 * Esquema Base de la BD EventFest
 */

/**
 * Author:  Albert Conesa
 */

--
-- BD: `eventfest`
--
CREATE DATABASE IF NOT EXISTS `eventfest` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `eventfest`;

-- ---------------------------------------------------------

--
-- Estructura taula `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int AUTO_INCREMENT NOT NULL,
  `user_login` varchar(25) UNIQUE NOT NULL,
  `user_pass` varchar(255) NOT NULL,
  `user_email` varchar(100) UNIQUE NOT NULL,
  `user_registered` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `user_role` int NOT NULL,
  `user_token` varchar(50) NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Inserció dades inicials a la taula `users`

-- pass md5 sadmin > c5edac1b8c1d58bad90a246d8f08f53b
-- pass md5 admin > 21232f297a57a5a743894a0e4a801fc3
-- pass md5 client > 62608e08adc29a8d6dbc9754e659f125
--
--
-- sadmin are code 0 > restrict delete of this user!!!
-- admin are code 1
-- users are code 2
--

INSERT INTO `users` (`id`, `user_login`, `user_pass`, `user_email`, `user_registered`, `user_role`, `user_token` ) VALUES
(1, 'sadmin', 'c5edac1b8c1d58bad90a246d8f08f53b', 'sadmin@ioc.xtec.com', '2018-02-27 00:00:00', 0, ''),
(2, 'admin', '21232f297a57a5a743894a0e4a801fc3', 'admin@ioc.xtec.com', '2018-02-28 00:00:00', 1, ''),
(3, 'georgina', '62608e08adc29a8d6dbc9754e659f125', 'georgina@ioc.xtec.com', '2018-03-02 00:00:00', 2, ''),
(4, 'carlos', '62608e08adc29a8d6dbc9754e659f125', 'carlos@ioc.xtec.com', '2018-03-02 00:00:00', 2, ''),
(5, 'albert', '62608e08adc29a8d6dbc9754e659f125', 'albert@ioc.xtec.com', '2018-03-02 00:00:00', 2, '');
