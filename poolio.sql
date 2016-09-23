-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 192.168.1.15
-- Generation Time: Jun 15, 2016 at 10:39 PM
-- Server version: 5.5.3-m3-community
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `poolio`
--

-- --------------------------------------------------------

--
-- Table structure for table `registrations`
--

CREATE TABLE IF NOT EXISTS `registrations` (
  `mobile` varchar(11) NOT NULL,
  `first_name` varchar(30) NOT NULL,
  `last_name` varchar(30) NOT NULL,
  `email` varchar(30) NOT NULL,
  `password` varchar(10) NOT NULL,
  `registration_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `id` int(8) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`),
  UNIQUE KEY `mobile` (`mobile`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=5 ;

--
-- Dumping data for table `registrations`
--

INSERT INTO `registrations` (`mobile`, `first_name`, `last_name`, `email`, `password`, `registration_time`, `id`) VALUES
('1234', 'asd', 'qweasdsws', 'qwewqe', '1234', '2016-06-10 19:41:43', 1),
('2', 'Yd', 'EU', 'fh', 'w', '2016-06-11 12:33:27', 2),
('Ery', 'Tc', 'R', 't', 'y', '2016-06-11 19:08:00', 3),
('9176044424', 'Sumit', 'Nihalani', 'ns@gmail.com', 'asdfg', '2016-06-14 23:27:09', 4);

-- --------------------------------------------------------

--
-- Table structure for table `rides`
--

CREATE TABLE IF NOT EXISTS `rides` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(11) NOT NULL,
  `source` varchar(100) NOT NULL,
  `destination` varchar(100) NOT NULL,
  `type` varchar(20) NOT NULL,
  `date` varchar(20) NOT NULL,
  `time` varchar(20) NOT NULL,
  `vehicle_name` varchar(40) NOT NULL,
  `vehicle_number` varchar(20) DEFAULT NULL,
  `seats` int(1) NOT NULL DEFAULT '1',
  `chargeable` tinyint(1) NOT NULL DEFAULT '1',
  `amount` int(5) DEFAULT '0',
  `offer_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `r_id` (`mobile`),
  KEY `mobile` (`mobile`),
  KEY `mobile_2` (`mobile`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Dumping data for table `rides`
--

INSERT INTO `rides` (`id`, `mobile`, `source`, `destination`, `type`, `date`, `time`, `vehicle_name`, `vehicle_number`, `seats`, `chargeable`, `amount`, `offer_time`) VALUES
(1, '9176044424', 'Abode Valley', 'Estancia', 'Bike', '16/06/2016', '01:47', 'R15', 'Rj 14 2564', 1, 1, 20, '2016-06-15 20:20:15');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `rides`
--
ALTER TABLE `rides`
  ADD CONSTRAINT `rides_ibfk_2` FOREIGN KEY (`mobile`) REFERENCES `registrations` (`mobile`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
