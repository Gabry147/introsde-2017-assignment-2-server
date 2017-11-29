package it.gabry147.entities;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "activityTypes")
public class ActivityTypes {
	@XmlElement(name = "activity_type")
	protected List<ActivityType> typeList;

	//@JsonProperty("activityTypes")
	public List<ActivityType> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<ActivityType> typeList) {
		this.typeList = typeList;
	} 
}
