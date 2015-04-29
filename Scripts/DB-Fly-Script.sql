-- phpMyAdmin SQL Dump
-- version 4.2.10
-- http://www.phpmyadmin.net
--
-- Host: localhost:8889
-- Generation Time: Apr 29, 2015 at 07:34 PM
-- Server version: 5.5.38
-- PHP Version: 5.6.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `Fly`
--

-- --------------------------------------------------------

--
-- Table structure for table `Biletter`
--

CREATE TABLE `Biletter` (
`id` int(11) NOT NULL,
  `passenger` varchar(50) NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=5941 DEFAULT CHARSET=latin1;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `Biletter`
--
ALTER TABLE `Biletter`
 ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `Biletter`
--
ALTER TABLE `Biletter`
MODIFY `id` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=5941;