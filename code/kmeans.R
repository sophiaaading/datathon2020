# 
# 
# data = read.csv("C:/Users/Qinyang/Desktop/part1_merge.csv")
# data = na.omit(data)
# 
# 
# 
# 
# 
# 
# # require(rgl,  lib.loc="C:/TFS/Rlib/")
# # require(plotrix,  lib.loc="C:/TFS/Rlib/")
# library(SciViews)
# library(corrplot)
# require(ggplot2)
# require(reshape)
# require("gridExtra")
# 
# # install.packages(c("reshape","corrplot"))
# 
# data$NAICS.id = as.numeric(data$NAICS.id)
# data$STATE = as.numeric(data$STATE)
# head(data)
# summary(data)
# 
# data = data[c(-4,-24)]
# colnames(data)[6] <- "AvgIncome"
# colnames(data)[7] <- "Density"
# colnames(data)[24] <- "African"
# colnames(data)[25] <- "Indian.Alaska"
# colnames(data)[27] <- "Hawaiian"
# 
# data.pca <- pcomp(~., data = data)
# d_pca = cbind(cbind(data, data.pca$scores), car = rownames(data))
# #d.pca
# # screeplot(data.pca)
# 
# #arrow plot
# plot(data.pca, which = "correlations")
# 
# #corr plot
# M<-cor(data)
# corrplot(M, method="circle")
# 
# #try to find best k
# # set.seed(124)
# # getWCSSData <- function(X) {
# #   wcss_values <- vector()
# #   max_wcss_steps = sqrt(length(X[,1]))
# #   for(i in 1:max_wcss_steps) {
# #     wcss_values[i] <- sum(kmeans(X, i, iter.max = 1000)$withinss)
# #   }
# #   return(wcss_values)
# # }
# # X = data[2:27]
# # wcss_values = getWCSSData(X)
# # 
# # nb_clusters = seq(1, length(wcss_values), 1)
# # getElbowPoint <- function(x_values, y_values) {
# #   
# #   # Max values to create line
# #   max_x_x <- max(x_values)
# #   max_x_y <- y_values[which.max(x_values)]
# #   max_y_y <- max(y_values)
# #   max_y_x <- x_values[which.max(y_values)]
# #   max_df <- data.frame(x = c(max_y_x, max_x_x), y = c(max_y_y, max_x_y))
# #   
# #   # Creating straight line between the max values
# #   fit <- lm(max_df$y ~ max_df$x)
# #   
# #   # Distance from point to line
# #   distances <- c()
# #   for(i in 1:length(x_values)) {
# #     distances <- c(distances, abs(coef(fit)[2]*x_values[i] - y_values[i] + coef(fit)[1]) / sqrt(coef(fit)[2]^2 + 1^2))
# #   }
# #   
# #   # Max distance point
# #   x_max_dist <- x_values[which.max(distances)]
# #   y_max_dist <- y_values[which.max(distances)]
# #   
# #   return(c(x_max_dist, y_max_dist, max(distances)))
# # }
# # elbowPoint_info = getElbowPoint(x_values = nb_clusters, y_values = wcss_values)
# # 
# # 
# # showElbowGraph(nb_clusters, wcss_values)
# 
# 
# 
# #view the results
# k <- kmeans(data, 5, nstart=25, iter.max=1000)
# new = cbind(data,cluster = k$cluster)
# 
# new = cbind(data,cluster = k$cluster)
# new = cbind(car = rownames(new),new)
# new = new[order(new$cluster),]
# #ordering of cars on cluster
# new$car <- factor(new$car, levels=(new$car)[order(new$cluster)])
# new = cbind(new, variable="cluster")
# 
# g = ggplot(new, aes(variable, car)) + geom_tile(aes(fill = cluster),
#                                                 colour = "white") + scale_fill_gradient(low = "red",
#                                                                                         high = "green") +  theme(legend.position="none")
# 
# myplotslist2 <- list()
# myplotslist2[[1]] = g
# var = c("Avg_total_income", "populationDensity", "Asian", "White")
# 
# for (i in 1:length(var)){
#   t= paste("new[[\"variable\"]] = \"", var[[i]],"\"; a = ggplot(new, aes(variable, car)) + geom_tile(aes(fill = ", var[[i]], "),colour = \"white\") + scale_fill_gradient(low = \"red\", high = \"green\") + theme(axis.title.y=element_blank(), axis.text.y=element_blank(),legend.position=\"none\"); myplotslist2[[i+1]] = a")
#   eval(parse(text=t))
# }
# 
# grid.arrange(grobs=myplotslist2, ncol=(length(var)+1), widths=rep(100,length(var)+1))


test = read.csv("C:/Users/Qinyang/Desktop/race.csv")
# state = c(levels(unique(test$state)))
row.names(newNew) = c(levels(unique(test$state)))
colnames(newNew) = c("White", "African","Asian")
# typeof(newNew$White)
# newdata = test[,-1]
# newNew = newdata[,-1]
newNew$White=  as.numeric(newNew$White)
newNew$African =  as.numeric(newNew$African)
newNew$Asian =  as.numeric(newNew$Asian)
df = scale(newNew)

# print(newNew[,1:2])

library(factoextra)
res.hk <-hkmeans(df, 3)
# names(res.hk)
# Visualize the hkmeans final clusters
fviz_cluster(res.hk, palette = "jco", repel = TRUE,
             ggtheme = theme_classic()) + ggtitle("KMeans Clustering for White, African and Asian")
dev.off()

names(res.hk)
