package org.mobon.billing.report;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class StreamTest {

	public static void main(String[] args) {
		List<Star> totalList= Arrays.asList(
				new Star("안양고","문근영")
				, new Star("안양고","신지")
				, new Star("안양고","이정현")
				, new Star("안양고","심은진")
				, new Star("성남고","김희선")
				, new Star("수원고","한지혜")
				, new Star("수원고","보아")
				);

		// 위의 코드와 같음
//		Map<String, List<String>> nestedMap= totalList.stream()
//				.collect(groupingBy(Star::getSchool
//						, mapping(Star::getKey, toList())));
//		System.out.println(nestedMap);
		
//		totalList.stream().collect(Collectors.groupingBy(EducationData::getSchool))
//		    .entrySet().stream()
//		    .collect(Collectors.toMap(x -> {
//		        int sumCnt= x.getValue().stream().mapToInt(EducationData::getCnt).sum();
//		        int sumPoint= x.getValue().stream().mapToInt(EducationData::getPoint).sum();
//		        return new EducationData(x.getKey(), sumCnt, sumPoint);
//		    }, Map.Entry::getValue));
	}

	static class Star implements Serializable{
		private String school;
		private String student;
		private int cnt;
		private int point;

		public Star(){		}
		public Star(String _school, String _student){
			school=_school;
			student=_student;
			setCnt(1);
			setPoint(1);
		}
		public Star(String _school, String _teacher, String _student, int _cnt, int _point){
			school=_school;
			student=_student;
			cnt=_cnt;
			point=_point;
		}
		
		public String getKey() {
			return String.format("%s", this.student);
		}
		public String getStudent() {
			return student;
		}
		public void setStudent(String student) {
			this.student = student;
		}
		public String getSchool() {
			return school;
		}
		public void setSchool(String school) {
			this.school = school;
		}
		public int getCnt() {
			return cnt;
		}
		public void setCnt(int cnt) {
			this.cnt = cnt;
		}
		public int getPoint() {
			return point;
		}
		public void setPoint(int point) {
			this.point = point;
		}

	}

}
