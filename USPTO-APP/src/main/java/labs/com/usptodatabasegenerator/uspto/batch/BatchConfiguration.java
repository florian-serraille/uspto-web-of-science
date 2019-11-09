package labs.com.usptodatabasegenerator.uspto.batch;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.converter.DefaultJobParametersConverter;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class BatchConfiguration implements ApplicationContextAware {

	private final JobRegistry jobRegistry;
	private final JobRepository jobRepository;
	private final JobExplorer jobExplorer;
	
	private ApplicationContext applicationContext;

	public BatchConfiguration(JobRegistry jobRegistry, JobRepository jobRepository, JobExplorer jobExplorer) {
		this.jobRegistry = jobRegistry;
		this.jobRepository = jobRepository;
		this.jobExplorer = jobExplorer;
	}

	@Bean
	public JobRegistryBeanPostProcessor jobRegistrar() throws Exception {
		JobRegistryBeanPostProcessor registrar = new JobRegistryBeanPostProcessor();

		registrar.setJobRegistry(this.jobRegistry);
		registrar.setBeanFactory(this.applicationContext.getAutowireCapableBeanFactory());
		registrar.afterPropertiesSet();

		return registrar;
	}

	@Bean(name = "simpleJobOperator")
	public JobOperator getJobOperator() throws Exception {

		SimpleJobOperator jobOperator = new SimpleJobOperator();
		jobOperator.setJobRegistry(jobRegistry);
		jobOperator.setJobRepository(jobRepository);
		jobOperator.setJobExplorer(jobExplorer);
		jobOperator.setJobLauncher(getJobLauncher());
		jobOperator.setJobParametersConverter(new DefaultJobParametersConverter());
		jobOperator.afterPropertiesSet();

		return jobOperator;
	}

	@Bean(name = "asyncLauncher")
	public JobLauncher getJobLauncher() throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
		jobLauncher.afterPropertiesSet();

		return jobLauncher;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
