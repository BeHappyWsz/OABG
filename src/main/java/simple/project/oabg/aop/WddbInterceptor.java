package simple.project.oabg.aop;

import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class WddbInterceptor {
//      
//	@Pointcut("execution(* simple.project.oabg.service.HypxsqService.doCzSh(..)) or "
//			+ "execution(* simple.project.oabg.service.HypxsqService.doBgsSh(..)) or "
//			+ "execution(* simple.project.oabg.service.HypxsqService.doCwSh(..)) or "
//			+ "execution(* simple.project.oabg.service.GwjdsqService.doCzSh(..)) or "
//			+ "execution(* simple.project.oabg.service.GwjdsqService.doBgsSh(..)) or "
//			+ "execution(* simple.project.oabg.service.GwjdsqService.doCwSh(..))")
//	private void shspMethod(){}
//	
////	@AfterReturning(value = "shspMethod()  &&  args(id,sftg,suggestion)",returning = "res")  
////    public void doSh(String id,String sftg,String suggestion,Result res){
////		//String id,String sftg,String suggestion,
////        System.out.println("执行了审核");  
////    }
//	
//	@AfterReturning(pointcut = "execution(* simple.project.oabg.service.*.doSp(..))", returning = "res")  
//    public void doSp(Result res){
//		//String id,String sftg,String suggestion,
//        System.out.println("执行了审批");  
//    }
//
//	@AfterReturning(
//			pointcut = "execution("
//					+ "* simple.project.oabg.service.*.deleteByIds(..)"
//					+ ")", 
//			returning = "res")  
//    public void doDelete(Result res){
//		//String ids,
//        System.out.println("执行了删除");  
//    } 
}
