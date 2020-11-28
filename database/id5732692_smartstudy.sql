-- phpMyAdmin SQL Dump
-- version 4.7.7
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Aug 05, 2018 at 08:47 AM
-- Server version: 10.2.12-MariaDB
-- PHP Version: 7.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `id5732692_smartstudy`
--

-- --------------------------------------------------------

--
-- Table structure for table `insti_info`
--

CREATE TABLE `insti_info` (
  `insti_id` int(2) NOT NULL,
  `insti_name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `class` int(2) NOT NULL,
  `medium` varchar(10) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `insti_info`
--

INSERT INTO `insti_info` (`insti_id`, `insti_name`, `class`, `medium`) VALUES
(1, 'kulkarni academy', 0, ''),
(2, 'mimoh academy', 0, ''),
(3, 'mayur academy', 0, '');

-- --------------------------------------------------------

--
-- Table structure for table `notes`
--

CREATE TABLE `notes` (
  `note_id` int(5) NOT NULL,
  `insti_id` int(2) NOT NULL,
  `is_video` int(1) NOT NULL,
  `subject_id` int(2) NOT NULL,
  `class` int(2) NOT NULL,
  `medium` varchar(10) CHARACTER SET latin1 NOT NULL,
  `chapter_id` int(11) NOT NULL,
  `note_name` varchar(20) CHARACTER SET latin1 NOT NULL,
  `upload_time` datetime NOT NULL,
  `note_type` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `notes`
--

INSERT INTO `notes` (`note_id`, `insti_id`, `is_video`, `subject_id`, `class`, `medium`, `chapter_id`, `note_name`, `upload_time`, `note_type`) VALUES
(1, 2, 0, 1, 10, 'english', 1, 'heat', '2018-06-03 10:00:00', 'pdf');

-- --------------------------------------------------------

--
-- Table structure for table `perinfo`
--

CREATE TABLE `perinfo` (
  `id` int(5) NOT NULL,
  `insti_id` int(2) NOT NULL,
  `mobileno` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(35) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `class` int(3) NOT NULL,
  `medium` varchar(12) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `batch` varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `rollno` varchar(5) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `bdate` date NOT NULL,
  `email` varchar(40) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `gender` varchar(6) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `issubscription` int(1) NOT NULL,
  `islogin` int(1) NOT NULL,
  `mac_address` varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `perinfo`
--

INSERT INTO `perinfo` (`id`, `insti_id`, `mobileno`, `name`, `password`, `class`, `medium`, `batch`, `rollno`, `bdate`, `email`, `gender`, `address`, `issubscription`, `islogin`, `mac_address`) VALUES
(1, 2, '8055680143', 'Mimoh Kulkarni', '123', 10, 'english', 'morning', '70', '2018-05-09', 'mmkk', 'male', 'pune', 0, 0, '00:EC:0A:85:3F:11'),
(2, 0, '9665214426', 'Mayur Kulkarni', '123', 10, 'english', 'evening', '70', '1992-10-02', 'mayurkulkarni16@gmail.com', 'Male', 'pune', 0, 0, '00:EC:0A:85:3F:11'),
(3, 0, '9822086074', 'Mangesh kulkarni', '123', 12, 'english', 'morning', '68', '1965-10-22', 'mimohkulkarni17@gmail.com', 'Male', 'Solapur', 0, 1, '00:EC:0A:85:3F:11'),
(4, 0, '9822016074', 'Mansi Kulkarni', '123', 10, 'english', 'morning', '37', '0000-00-00', 'mmk@gmail.com', 'Female', 'solapur', 0, 0, '5C:99:60:B5:18:29'),
(5, 0, '9822086077', 'Abc xyz', '123', 10, 'english', 'morning', '68', '1995-08-03', 'gjvrujbv@gmail.com', 'Male', 'fubft', 0, 0, '00:EC:0A:85:3F:11'),
(6, 0, '1212121212', 'hhk', '123', 28, 'english', 'morning', '57', '2018-05-30', 'ghn', 'Male', 'yhh', 0, 0, '00:EC:0A:85:3F:11'),
(8, 0, '1122334455', 'ram patil', '123', 11, 'semi-english', 'morning', '59', '1994-08-11', 'ram@gmail.com', 'Male', 'solapur', 0, 0, '00:EC:0A:85:3F:11'),
(9, 0, '1234512345', 'ram patil', '123', 12, 'semi-english', 'morning', '59', '1991-07-18', 'ram@gmail.com', 'Male', 'solapur', 0, 0, '00:EC:0A:85:3F:11'),
(12, 1, '1234123412', 'ram patil', '123', 12, 'semi-english', 'morning', '59', '2005-03-10', 'ram@gmail.com', 'Male', 'solapur', 0, 0, '00:EC:0A:85:3F:11'),
(14, 1, '1234567890', 'ram patil', '1234', 11, 'semi-english', 'evening', '70', '0000-00-00', 'ram@gmail.com', 'Male', 'solapur', 0, 0, '00:EC:0A:85:3F:11'),
(15, 2, '5454545454', 'mayur kulkarni', '123', 10, 'english', 'morning', '52', '0000-00-00', 'mimoh@gmailk.com', 'Male', 'solapur', 0, 1, '02:00:00:00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `perinfo_superior`
--

CREATE TABLE `perinfo_superior` (
  `id` int(5) NOT NULL,
  `insti_id` int(2) NOT NULL,
  `name` varchar(50) CHARACTER SET latin1 NOT NULL,
  `mobileno` varchar(12) CHARACTER SET latin1 NOT NULL,
  `password` varchar(20) CHARACTER SET latin1 NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `perinfo_superior`
--

INSERT INTO `perinfo_superior` (`id`, `insti_id`, `name`, `mobileno`, `password`) VALUES
(1, 2, 'mimoh kulkarni', '8055680143', '123');

-- --------------------------------------------------------

--
-- Table structure for table `raw_perinfo`
--

CREATE TABLE `raw_perinfo` (
  `id` int(5) NOT NULL,
  `insti_id` int(2) NOT NULL,
  `mobileno` varchar(10) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(35) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `class` int(3) NOT NULL,
  `medium` varchar(12) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `batch` varchar(15) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `rollno` varchar(5) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `bdate` date NOT NULL,
  `email` varchar(40) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `gender` varchar(6) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `mac_address` varchar(25) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `smslog`
--

CREATE TABLE `smslog` (
  `id` int(10) NOT NULL,
  `insti_id` int(2) NOT NULL,
  `mobileno` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `msg` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `date_time` datetime NOT NULL,
  `type` int(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `smslog`
--

INSERT INTO `smslog` (`id`, `insti_id`, `mobileno`, `msg`, `date_time`, `type`) VALUES
(2, 2, '8055680143', '123456', '2018-07-27 14:02:05', 1),
(3, 0, '8055680143', '', '2018-07-28 06:33:13', 1),
(4, 0, '8055680143', '', '2018-07-28 06:33:15', 1);

-- --------------------------------------------------------

--
-- Table structure for table `syllabus`
--

CREATE TABLE `syllabus` (
  `insti_id` int(3) NOT NULL,
  `class` int(3) NOT NULL,
  `medium` varchar(12) NOT NULL,
  `sub_id` int(2) NOT NULL,
  `sub_name` varchar(20) NOT NULL,
  `chapter_id` int(4) NOT NULL,
  `chapter_name` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `syllabus`
--

INSERT INTO `syllabus` (`insti_id`, `class`, `medium`, `sub_id`, `sub_name`, `chapter_id`, `chapter_name`) VALUES
(2, 10, 'english', 3, 'physics', 2, 'light'),
(2, 10, 'english', 2, 'chemistry', 5, 'bio'),
(2, 10, 'english', 2, 'chemistry', 2, 'heat reaction'),
(2, 10, 'english', 1, 'maths', 1, 'addition');

-- --------------------------------------------------------

--
-- Table structure for table `test`
--

CREATE TABLE `test` (
  `test_id` int(11) NOT NULL,
  `insti_id` int(1) NOT NULL,
  `class` int(3) NOT NULL,
  `medium` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `test_subject` int(2) NOT NULL,
  `test_chapter` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `test_name` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `test_date_time` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `test`
--

INSERT INTO `test` (`test_id`, `insti_id`, `class`, `medium`, `test_subject`, `test_chapter`, `test_name`, `test_date_time`) VALUES
(1, 2, 10, 'english', 1, '1,2,3', 'one', '2018-07-31 10:30:00'),
(2, 2, 10, 'english', 1, '2,3', 'two', '2018-06-02 22:38:53'),
(3, 2, 10, 'english', 2, '5', 'three', '2018-06-02 22:38:56'),
(4, 2, 10, 'english', 1, '1', 'c1', '2018-06-21 16:18:23'),
(5, 2, 10, 'english', 2, '11', 'p6', '2018-06-23 10:28:35'),
(6, 2, 10, 'english', 3, '1', '', '2018-06-23 10:28:39'),
(7, 2, 10, 'english', 2, '1,5', 'p8', '2018-06-30 10:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `test_questions`
--

CREATE TABLE `test_questions` (
  `id` int(10) NOT NULL,
  `insti_id` int(2) NOT NULL,
  `test_id` int(5) NOT NULL,
  `sub_id` int(2) NOT NULL,
  `chapter_id` int(2) NOT NULL,
  `class` int(2) NOT NULL,
  `medium` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `question` varchar(200) CHARACTER SET utf8 NOT NULL,
  `ans_a` varchar(50) CHARACTER SET utf8 NOT NULL,
  `ans_b` varchar(50) CHARACTER SET utf8 NOT NULL,
  `ans_c` varchar(50) CHARACTER SET utf8 NOT NULL,
  `ans_d` varchar(50) CHARACTER SET utf8 NOT NULL,
  `ans_correct` int(1) NOT NULL,
  `image_info` varchar(5) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `test_questions`
--

INSERT INTO `test_questions` (`id`, `insti_id`, `test_id`, `sub_id`, `chapter_id`, `class`, `medium`, `question`, `ans_a`, `ans_b`, `ans_c`, `ans_d`, `ans_correct`, `image_info`) VALUES
(1, 1, 1, 1, 1, 10, 'english', 'What is your name?', 'Mimoh', 'Mayur', 'Mangesh', 'Mansi', 1, '00000'),
(2, 1, 0, 1, 1, 10, 'english', 'What is your surname?', 'Ingole', 'Joshi', 'Kulkarni', 'Deshmukh', 3, '00000'),
(3, 1, 1, 0, 0, 10, 'english', '	I ..... tennis every Sunday morning.', 'Playing', 'Play', 'Am playing', 'Am play', 2, '00000'),
(4, 1, 0, 1, 1, 10, 'english', 'Ram ..... his teeth before breakfast every morning.', 'Will cleaned', 'Is cleaning', 'Cleans', 'Clean', 3, '00000'),
(5, 1, 0, 1, 1, 10, 'english', 'Don\'t make so much noise. Noriko ..... to study for her ESL test!', 'try', 'tries', 'tried', 'is trying', 4, '00000'),
(6, 1, 1, 1, 1, 10, 'english', 'Sorry, she can\'t come to the phone. She ..... a bath!', 'Is having', 'Having', 'Have', 'Has', 1, '10000'),
(7, 1, 0, 1, 1, 10, 'english', '	Babies ..... when they are hungry.', 'Cry', 'Cried', 'Cries', 'Are crying', 1, '01000'),
(8, 1, 0, 1, 1, 10, 'english', 'Jane ..... her blue jeans today, but usually she wears a skirt or a dress.', 'Wears', 'Wearing', 'Wear', 'Is wearing', 4, '10000'),
(9, 1, 0, 0, 0, 10, 'english', 'What time .....', 'The train leaves?', 'Leaves the train?', 'Is the train leaving?', 'Does the train leave?', 4, '00000'),
(10, 1, 0, 0, 0, 10, 'english', 'I ..... for my pen. Have you seen it?', 'Will look', 'Looking', 'Look', 'Am looking', 4, '00000'),
(11, 1, 0, 0, 0, 10, 'english', 'युकी भांवरी का संंबंध किस खेल से है?', 'टेनिस', 'तैराकी', 'बैडमिंटन', 'क्रिकेट', 1, '00000'),
(12, 1, 0, 0, 0, 10, 'english', 'अर्जुन पुरस्कार किस क्षेत्र में उत्कृष्टता के लिए प्रदान किए जाते हैं?', 'सिनेमा', 'साहित्य', 'खेल-कूद', 'विज्ञान', 3, '00000');

-- --------------------------------------------------------

--
-- Table structure for table `test_result`
--

CREATE TABLE `test_result` (
  `id` int(3) NOT NULL,
  `mobileno` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `test_id` int(2) NOT NULL,
  `sub_id` int(2) NOT NULL,
  `chapter_id` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `marks` int(3) NOT NULL,
  `max_marks` int(3) NOT NULL,
  `test_date_time` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `test_result`
--

INSERT INTO `test_result` (`id`, `mobileno`, `test_id`, `sub_id`, `chapter_id`, `marks`, `max_marks`, `test_date_time`) VALUES
(1, '5454545454', 0, 1, '1', 0, 5, '2018-07-05 23:09:08'),
(2, '5454545454', 0, 1, '1', 2, 5, '2018-07-05 23:12:09'),
(3, '5454545454', 0, 1, '1', 3, 5, '2018-07-05 23:13:31'),
(4, '8055680143', 0, 1, '1', 5, 5, '2018-07-06 09:15:15'),
(5, '8055680143', 0, 1, '1', 0, 5, '2018-07-06 22:10:16');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `notes`
--
ALTER TABLE `notes`
  ADD PRIMARY KEY (`note_id`);

--
-- Indexes for table `perinfo`
--
ALTER TABLE `perinfo`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `perinfo_superior`
--
ALTER TABLE `perinfo_superior`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `raw_perinfo`
--
ALTER TABLE `raw_perinfo`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `smslog`
--
ALTER TABLE `smslog`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `test`
--
ALTER TABLE `test`
  ADD PRIMARY KEY (`test_id`);

--
-- Indexes for table `test_questions`
--
ALTER TABLE `test_questions`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `test_result`
--
ALTER TABLE `test_result`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `notes`
--
ALTER TABLE `notes`
  MODIFY `note_id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `perinfo`
--
ALTER TABLE `perinfo`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=16;

--
-- AUTO_INCREMENT for table `perinfo_superior`
--
ALTER TABLE `perinfo_superior`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `raw_perinfo`
--
ALTER TABLE `raw_perinfo`
  MODIFY `id` int(5) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `smslog`
--
ALTER TABLE `smslog`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `test`
--
ALTER TABLE `test`
  MODIFY `test_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `test_questions`
--
ALTER TABLE `test_questions`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `test_result`
--
ALTER TABLE `test_result`
  MODIFY `id` int(3) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
