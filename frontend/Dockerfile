
FROM node:20 AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY angular.json .
COPY tsconfig.json .
COPY tsconfig.app.json .
COPY src ./src
COPY public ./public
RUN npm run build -- --configuration production



FROM nginx:1.29.3-alpine
COPY --from=build /app/dist/frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
