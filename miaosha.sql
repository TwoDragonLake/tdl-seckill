/*
SQLyog Ultimate v12.09 (64 bit)
MySQL - 5.5.10-log : Database - miaosha
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`miaosha` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci */;

USE `miaosha`;

/*Table structure for table `goods` */

DROP TABLE IF EXISTS `goods`;

CREATE TABLE `goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '商品名字',
  `random_name` char(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '随机名称，该名称作为抢购的商品唯一标志，好处是防止被刷单这提前知晓链接，降低被刷单可能性',
  `store` int(11) DEFAULT NULL COMMENT '库存量',
  `start_time` datetime DEFAULT NULL COMMENT '活动开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '活动结束时间',
  `version` int(11) DEFAULT '0' COMMENT '版本号',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标志，0-未删除，1-已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Data for the table `goods` */

insert  into `goods`(`id`,`name`,`random_name`,`store`,`start_time`,`end_time`,`version`,`del_flag`) values (1,'第一个商品','0e67e331-c521-406a-b705-64e557c4c06c',249999,'2017-05-01 22:41:37','2017-05-05 22:41:47',1,0),(2,'第二个商品','fc3536f6-3e8f-4924-ac88-6cf662faf61e',1000,'2017-05-03 22:41:37','2017-05-04 22:41:47',0,0),(3,'第三个商品','629bef27-dcdb-48c1-ab03-466c8056b912',996,'2017-05-02 22:41:37','2017-05-03 22:41:47',4,0),(4,'第四个商品','8e694baa-6cd8-4044-b858-87415c2e1293',997,'2017-05-01 22:41:37','2017-05-06 22:41:47',3,0),(6,'第五个商品','f29bef27-dcdb-h8c1-ab03-466c8056d3c1',9999,'2017-05-02 23:50:08','2017-05-05 23:50:19',2,0);

/*Table structure for table `order` */

DROP TABLE IF EXISTS `order`;

CREATE TABLE `order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `mobile` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '用户手机号',
  `goods_id` int(11) DEFAULT NULL COMMENT '商品ID',
  `num` int(8) DEFAULT NULL COMMENT '数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `del_flag` tinyint(1) DEFAULT '0' COMMENT '删除标识，0-未删除，1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_mobile_goodsid` (`mobile`,`goods_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

/*Data for the table `order` */

insert  into `order`(`id`,`mobile`,`goods_id`,`num`,`create_time`,`del_flag`) values (1,'17052191899',1,1,'2017-05-04 00:43:12',0);

/* Procedure structure for procedure `pro_doorder` */

/*!50003 DROP PROCEDURE IF EXISTS  `pro_doorder` */;

DELIMITER $$

/*!50003 CREATE DEFINER=`root`@`localhost` PROCEDURE `pro_doorder`(IN i_goods_id BIGINT,IN i_mobile varchar(11),IN i_order_time TIMESTAMP ,OUT o_result INT(1) ,OUT o_order_id INT(11))
BEGIN
	  DECLARE insert_count INT DEFAULT 0;
	    START TRANSACTION ;
	    INSERT IGNORE INTO `order`(mobile,goods_id,num,create_time)VALUES(i_mobile,i_goods_id,1,i_order_time);
	    SELECT LAST_INSERT_ID() INTO o_order_id;	    
	    SELECT ROW_COUNT() INTO insert_count;
	    
	    IF(insert_count = 0)THEN
	     ROLLBACK ;
	     SET o_result=-1;
	    ELSEIF(insert_count<0)THEN
	     ROLLBACK ;
	     SET o_result=-2;
	    ELSE
	     UPDATE goods
	     SET store = store - 1,
		 VERSION = VERSION+1
	     WHERE id = i_goods_id
	       AND end_time > i_order_time 
	       AND start_time < i_order_time
	       AND store>0;
	    SELECT ROW_COUNT() INTO insert_count;
	     IF(insert_count<=0)THEN
	      ROLLBACK ;
	      SET o_result = -2;
	     ELSE
	      COMMIT ;
	      SET o_result = 1;
	     END IF;
	  END IF;
	END */$$
DELIMITER ;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
