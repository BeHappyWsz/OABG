package simple.project.oabg.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import simple.project.oabg.dic.model.Zllx;
import simple.system.simpleweb.module.system.file.model.FileInfo;
import simple.system.simpleweb.platform.annotation.Des;
import simple.system.simpleweb.platform.model.table.AutoIDModel;

/**
 * 资料中心
 * 2017年9月5日
 * @author yc
 */
@Entity
@Table(name="t_zlzx")
public class Zlzx extends AutoIDModel{
	private static final long serialVersionUID = 1L;
	
	@Des("标题")
	@Setter
	@Getter
	@Column(name="title",length=50)
	private String title;
	
	@Des("资料类型")
	@Setter
	@Getter
	@ManyToOne
	private Zllx zllx;
	
	@Des("上传时间")
	@Setter
	@Getter
	@Column(name="scsj")
	private Date scsj;
	
	@Des("上传用户")
	@Setter
	@Getter
	@Column(name="userName",length=100)
	private String userName;
	
	@Des("上传用户名称")
	@Setter
	@Getter
	@Column(name="userRealName",length=200)
	private String userRealName;
	
	@Des("文件")
	@Setter
	@Getter
	@OneToOne
	private FileInfo fileInfo;
	
	@Des("文件说明")
	@Setter
	@Getter
	@Column(name="wjsm",length=500)
	private String wjsm;
	
}
