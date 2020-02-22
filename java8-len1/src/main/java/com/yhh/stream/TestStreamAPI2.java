package com.yhh.stream;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.junit.Test;
import com.yhh.stream.Employee.Status;


public class TestStreamAPI2 {


	List<Employee> emps = Arrays.asList(
			new Employee(102, "李四", 79, 6666.66, Status.BUSY),
			new Employee(101, "张三", 18, 9999.99, Status.FREE),
			new Employee(103, "王五", 28, 3333.33, Status.VOCATION),
			new Employee(104, "赵六", 8, 7777.77, Status.BUSY),
			new Employee(104, "赵六", 8, 7777.77, Status.FREE),
			new Employee(104, "赵六", 8, 7777.77, Status.FREE),
			new Employee(105, "田七", 38, 5555.55, Status.BUSY)
	);
	
	//3. 终止操作
	/**
		归约
		reduce(T identity, BinaryOperator) / reduce(BinaryOperator) ——可以将流中元素反复结合起来，得到一个值。
	 */
	@Test
	public void test1(){
		List<Integer> list = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		
		Integer sum = list.stream().reduce(0, (x, y) -> x + y);//0起始值 0传给x 1传给y 0+1=1在传给x 2传给y
		System.out.println(sum);
		
		System.out.println("----------------------------------------");

		//有可能Null时就返回Optional
		Optional<Double> op = emps.stream()
			.map(Employee::getSalary)
			.reduce(Double::sum);
		System.out.println(op.get());
	}
	
	//需求：搜索名字中 “六” 出现的次数
	@Test
	public void test2(){
		Optional<Integer> sum = emps.stream()
			.map(Employee::getName)
			.flatMap(TestStreamAPI::filterCharacter)
			.map((ch) -> {
				if(ch.equals('六'))
					return 1;
				else 
					return 0;
			}).reduce(Integer::sum);
		
		System.out.println(sum.get());
	}

    /**
     *  collect——将流转换为其他形式。接收一个 Collector接口的实现，用于给Stream中元素做汇总的方法
     */
	@Test
	public void test3(){
		List<String> list = emps.stream()
			.map(Employee::getName)
			.collect(Collectors.toList());
		list.forEach(System.out::println);
		
		System.out.println("----------------------------------");
		
		Set<String> set = emps.stream()
			.map(Employee::getName)
			.collect(Collectors.toSet());
		set.forEach(System.out::println);

		System.out.println("----------------------------------");

		//指定使用哪个集合
		HashSet<String> hs = emps.stream()
			.map(Employee::getName)
			.collect(Collectors.toCollection(HashSet::new));
		hs.forEach(System.out::println);
	}
	
	@Test
	public void test4(){

        //总数
        Long count = emps.stream()
                .collect(Collectors.counting());
        System.out.println(count);

        System.out.println("--------------------------------------------");

        // 工资获取平均值
        Double avg = emps.stream()
                .collect(Collectors.averagingDouble(Employee::getSalary));
        System.out.println(avg);

        System.out.println("--------------------------------------------");

        //工资总和
        Double sum = emps.stream()
                .collect(Collectors.summingDouble(Employee::getSalary));
        System.out.println(sum);

        System.out.println("--------------------------------------------");

        //获取工资最高工资
        Optional<Double> salary = emps.stream()
			                        .map(Employee::getSalary)
			                        .collect(Collectors.maxBy(Double::compare));
		System.out.println(salary.get());

        System.out.println("--------------------------------------------");

        //获取工资最高工资的人员信息
		Optional<Employee> op = emps.stream()
                .collect(Collectors.maxBy((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary())));
		System.out.println(op.get());

        Optional<Double> od = emps.stream()
                .map(Employee::getSalary)
                .collect(Collectors.maxBy(Double::compare));

        System.out.println(od.get());

        System.out.println("--------------------------------------------");

        //统计工资的最大值 、最小值、 平均值、 总数.....
        DoubleSummaryStatistics dss = emps.stream()
			.collect(Collectors.summarizingDouble(Employee::getSalary));
		System.out.println(dss.getMax());
	}

    /**
     * 分组
     */
	@Test
	public void test5(){
		Map<Status, List<Employee>> map = emps.stream()
			.collect(Collectors.groupingBy(Employee::getStatus));
		System.out.println(map);

        Function<Employee, Status> f = Employee::getStatus;
        Map<Status, List<Employee>> map2 = emps.stream().collect(Collectors.groupingBy(f));
        System.out.println(map2);
	}
	
	//多级分组
	@Test
	public void test6(){
		Map<Status, Map<String, List<Employee>>> map = emps.stream()
			.collect(Collectors.groupingBy(Employee::getStatus, Collectors.groupingBy((e) -> {
				if(e.getAge() >= 60)
					return "老年";
				else if(e.getAge() >= 35)
					return "中年";
				else
					return "成年";
			})));
		
		System.out.println(map);
	}

    /**
     *  分区(收集)
     */
	@Test
	public void test7(){
		Map<Boolean, List<Employee>> map = emps.stream()
			.collect(Collectors.partitioningBy((e) -> e.getSalary() >= 5000));
		System.out.println(map);

        Predicate<Employee> p = e -> e.getSalary() >= 5000;

        Map<Boolean, List<Employee>> map2 = emps.stream()
                .collect(Collectors.partitioningBy(p));
        System.out.println(map2);
	}
	
	//
	@Test
	public void test8(){
		String str = emps.stream()
			.map(Employee::getName)
			.collect(Collectors.joining("," , "----", "----"));
		System.out.println(str);
	}
	
	@Test
	public void test9(){
		Optional<Double> sum = emps.stream()
			.map(Employee::getSalary)
			.collect(Collectors.reducing(Double::sum));
		System.out.println(sum.get());
	}
}
