package com.yhh.stream;

import java.util.*;
import java.util.stream.Stream;
import org.junit.Test;
import com.yhh.stream.Employee.Status;

/**
 * 一、Stream API 的操作步骤：
 * 
 * 1. 创建 Stream
 * 
 * 2. 中间操作
 * 
 * 3. 终止操作(终端操作)
 */
public class TestStreamAPI {

    /**
     *  1. 创建 Stream
     */

	@Test
	public void test1(){
		//1. Collection 提供了两个方法  stream() 与 parallelStream()
		List<String> list = new ArrayList<>();
		Stream<String> stream = list.stream(); //获取一个串行流
		Stream<String> parallelStream = list.parallelStream(); //获取一个并行流
		
		//2. 通过 Arrays 中的 stream() 获取一个数组流
		Integer[] nums = new Integer[10];
		Stream<Integer> stream1 = Arrays.stream(nums);
		
		//3. 通过 Stream 类中静态方法 of()
		Stream<Integer> stream2 = Stream.of(1,2,3,4,5,6);
		String[] a = {"aa","nnn"};
        Stream<String> stream22 = Stream.of(a);

        //4. 创建无限流
		//迭代  不加limit就无限运行下去了
		Stream<Integer> stream3 = Stream.iterate(0, (x) -> x + 2).limit(10);
		stream3.forEach(System.out::println);

		//5. 生成
		Stream<Double> stream4 = Stream.generate(Math::random).limit(2);
		stream4.forEach(System.out::println);
		
	}
	

	List<Employee> emps = Arrays.asList(
			new Employee(102, "李四", 59, 6666.66),
			new Employee(101, "张三", 18, 9999.99),
			new Employee(103, "王五", 28, 3333.33),
			new Employee(104, "赵4", 8, 7777.77),
			new Employee(107, "赵7", 8, 7777.77),
			new Employee(106, "赵六", 8, 7777.77),
            new Employee(106, "赵六", 8, 7777.77),
			new Employee(105, "田七", 38, 5555.55)
	);

    /**
     *  2. 中间操作
     */

	/*
	  筛选与切片
		filter()——接收 Lambda ， 从流中排除某些元素。
		limit(n)—— 截断流，使其元素不超过给定数量。
		skip(n) —— 跳过元素，返回一个扔掉了前 n 个元素的流。若流中元素不足 n 个，则返回一个空流。与 limit(n) 互补
		distinct()——筛选，通过流所生成元素的 hashCode() 和 equals() 去除重复元素
	 */
	
	//内部迭代：迭代操作 Stream API 内部完成
	@Test
	public void test2(){
		//所有的中间操作不会做任何的处理
		Stream<Employee> stream = emps.stream()
			.filter((e) -> {
				System.out.println("测试中间操作");
				return e.getAge() <= 35;
			});
		
		//注意：只有当做终止操作时，所有的中间操作会一次性的全部执行，称为“惰性求值”
		stream.forEach(System.out::println); //没有这个中断终止操作上面的中间操作不会执行。
	}
	
	//外部迭代
	@Test
	public void test3(){
		Iterator<Employee> it = emps.iterator();
		while(it.hasNext()){
			System.out.println(it.next());
		}
	}
	
	@Test
	public void test4(){
		emps.stream()
			.filter((e) -> {
				System.out.println("短路！"); // &&  ||   这里的迭代执行limit次，后续的没必要执行了
				return e.getSalary() >= 5000;
			}).limit(3)
			.forEach(System.out::println);
	}
	
	@Test
	public void test5(){
		emps.parallelStream()
			.filter((e) -> e.getSalary() >= 5000)
			.skip(2)
			.forEach(System.out::println);
	}

	//distinct()
	@Test
	public void test6(){
		emps.stream()
			.distinct()
			.forEach(System.out::println);
	}


    /*
        映射
        map——接收 Lambda ， 将元素转换成其他形式或提取信息。接收一个函数作为参数，该函数会被应用到每个元素上，并将其映射成一个新的元素。
        flatMap——接收一个函数作为参数，将流中的每个值都换成另一个流，然后把所有流连接成一个流
     */
    @Test
    public void test7(){

        List<String> strList = Arrays.asList("aaa", "bbb", "ccc", "ddd", "eee");

        Stream<String> stream = strList.stream().map(String::toUpperCase);
        stream.forEach(System.out::println);

        System.out.println("---------------------------------------------");

        Stream<Stream<Character>> stream2 = strList.stream().map(TestStreamAPI::filterCharacter); // {{"aa","bb"}, {"cc","dd"}}
        stream2.forEach((sm) -> {
            // sm = Stream<Character>
            sm.forEach(System.out::println);
        });

        System.out.println("---------------------------------------------");

        Stream<Character> stream3 = strList.stream().flatMap(TestStreamAPI::filterCharacter); // {"aa","bb","cc","dd"}
        stream3.forEach(System.out::println);

        //类似于 list.add(list2)  list.addAll(list2)
    }

    public static Stream<Character> filterCharacter(String str){
        List<Character> list = new ArrayList<>();
        for (Character ch : str.toCharArray()) {
            list.add(ch);
        }
        return list.stream();
    }

    /*
        sorted()——自然排序 Comparable  被排序的元素必须实现了Comparable接口
        sorted(Comparator com)——定制排序
     */
    @Test
    public void test8(){
        emps.stream()
                .map(Employee::getName)
                .sorted()
                .forEach(System.out::println);

        System.out.println("------------------------------------");

        emps.stream()
                .sorted((x, y) -> {
                    if(x.getAge() == y.getAge()){
                        return x.getName().compareTo(y.getName());
                    }else{
                        return Integer.compare(x.getAge(), y.getAge());
                    }
                }).forEach(System.out::println);
    }



    List<Employee> emps2 = Arrays.asList(
            new Employee(102, "李四", 59, 6666.66, Status.BUSY),
            new Employee(101, "张三", 18, 9999.99, Status.FREE),
            new Employee(103, "王五", 28, 3333.33, Status.VOCATION),
            new Employee(104, "赵六", 8, 7777.77, Status.BUSY),
            new Employee(104, "赵六", 8, 7777.77, Status.FREE),
            new Employee(104, "赵六", 8, 7777.77, Status.FREE),
            new Employee(105, "田七", 38, 5555.55, Status.BUSY)
    );

    /**
     *  3. 终止操作
     */

	/*
		allMatch——检查是否匹配所有元素
		anyMatch——检查是否至少匹配一个元素
		noneMatch——跟allMatch相反，判断条件里的元素，所有的都不是，返回true
	 */
    @Test
    public void test9(){
        boolean bl = emps2.stream()
                .allMatch((e) -> e.getStatus().equals(Status.BUSY));
        System.out.println(bl);

        boolean bl1 = emps2.stream()
                .anyMatch((e) -> e.getStatus().equals(Status.BUSY));
        System.out.println(bl1);

        boolean bl2 = emps2.stream()
                .noneMatch((e) -> e.getStatus().equals(Status.BUSY));
        System.out.println(bl2);
    }

    /*
		findFirst——返回第一个元素
		findAny——返回当前流中的任意元素
	 */
    @Test
    public void test10(){
        Optional<Employee> op = emps2.stream()
                .sorted((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()))
                .findFirst();
        Employee e1 = op.get();
        Employee e2 = op.orElse(new Employee());  //可以放在空指针异常
        System.out.println(e2);

        System.out.println("--------------------------------");

        Optional<Employee> op2 = emps2.parallelStream()
                .filter((e) -> e.getStatus().equals(Status.FREE))
                .findAny();

        System.out.println(op2.get());
    }

    /*
		count——返回流中元素的总个数
		max——返回流中最大值
		min——返回流中最小值
	 */
    @Test
    public void test11(){
        long count = emps2.stream()
                .filter((e) -> e.getStatus().equals(Status.FREE))
                .count();
        System.out.println(count);

        //获取最大的工资是多少
        Optional<Double> op = emps2.stream()
                .map(Employee::getSalary)
                .max(Double::compare);
        System.out.println(op.get());

        //获取最大工资的人员信息
        Optional<Employee> op2 = emps2.stream()
                .max((e1, e2) -> Double.compare(e1.getSalary(), e2.getSalary()));
        System.out.println(op2.get());
    }

    //注意：流进行了终止操作后，不能再次使用
    @Test
    public void test12(){
        Stream<Employee> stream = emps2.stream()
                .filter((e) -> e.getStatus().equals(Status.FREE));

        long count = stream.count();
        System.out.println(count);

        stream.map(Employee::getSalary)
                .max(Double::compare);
    }




}
