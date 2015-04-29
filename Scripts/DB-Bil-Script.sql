-- phpMyAdmin SQL Dump
-- version 4.2.10
-- http://www.phpmyadmin.net
--
-- Host: localhost:8889
-- Generation Time: Apr 29, 2015 at 07:31 PM
-- Server version: 5.5.38
-- PHP Version: 5.6.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `Bil`
--

-- --------------------------------------------------------

--
-- Table structure for table `Biler`
--

CREATE TABLE `Biler` (
`id` int(11) NOT NULL,
  `farge` text NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=6040 DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Biler`
--
ALTER TABLE `Biler`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Biler`
--
ALTER TABLE `Biler`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=6040;