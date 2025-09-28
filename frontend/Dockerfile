FROM node:24.9.0-alpine

WORKDIR /app
COPY package*.json ./
RUN npm install


CMD ["npm", "start"]
#COPY . .
#RUN ng build --prod
#FROM nginx:stable-alpine
#COPY --from=build /app/dist/frontend /usr/share/nginx/html
#COPY nginx.conf /etc/nginx/conf.d/default.conf
#EXPOSE 80